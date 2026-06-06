import { useState, useEffect } from 'react';

// ==========================================
// CONFIGURAÇÃO DE AMBIENTE
// Lendo a URL do backend a partir do arquivo .env do Vite
// ==========================================
const API_URL = import.meta.env.VITE_API_BASE_URL;

function App() {
  // ==========================================
  // 1. O CÉREBRO: ESTADOS E VARIÁVEIS
  // ==========================================
  const [telaAtual, setTelaAtual] = useState('home');
  const [nomeUsuario, setNomeUsuario] = useState('');
  const [usuarioLogado, setUsuarioLogado] = useState(null);
  
  const [nomeSessao, setNomeSessao] = useState('');
  const [minhasSessoes, setMinhasSessoes] = useState([]);
  const [sessaoAtual, setSessaoAtual] = useState(null);

  const [categorias, setCategorias] = useState([]);
  const [nomeCategoria, setNomeCategoria] = useState('');
  const [pesoCategoria, setPesoCategoria] = useState(1.0);
  const [categoriaEditando, setCategoriaEditando] = useState(null);

  const [musicas, setMusicas] = useState([]);
  const [nomeMusicaNova, setNomeMusicaNova] = useState('');
  const [artistaMusicaNova, setArtistaMusicaNova] = useState(''); 
  const [notasTemporarias, setNotasTemporarias] = useState({});

  // ==========================================
  // 2. OS ESPIÕES: EFEITOS (USE EFFECT)
  // ==========================================
  useEffect(() => {
    if (telaAtual === 'dashboard' && usuarioLogado) {
      buscarMinhasSessoes();
    }
  }, [telaAtual, usuarioLogado]);

  useEffect(() => {
    if (telaAtual === 'detalhesSessao' && sessaoAtual) {
      buscarCategorias();
      buscarMusicas();
    }
  }, [telaAtual, sessaoAtual]);

  // ==========================================
  // 3. OS MÚSCULOS: FUNÇÕES LÓGICAS
  // ==========================================

  const fazerLogin = async () => {
    if (nomeUsuario.trim() === '') return alert("Por favor, digite um nome!");
    try {
      const resposta = await fetch(`${API_URL}/api/usuarios/entrar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nome: nomeUsuario }) 
      });
      const dados = await resposta.json();
      setUsuarioLogado(dados);
      setTelaAtual('dashboard');
    } catch (erro) {
      alert("Erro ao conectar ao servidor.");
    }
  };

  const buscarMinhasSessoes = async () => {
    try {
      const resposta = await fetch(`${API_URL}/api/sessoes/usuario/${usuarioLogado.id}`);
      if (resposta.ok) setMinhasSessoes(await resposta.json());
    } catch (erro) {
      console.error(erro);
    }
  };

  const criarNovaSessao = async () => {
    if (nomeSessao.trim() === '') return alert("Dê um nome à sessão!");
    try {
      await fetch(`${API_URL}/api/sessoes?usuarioId=${usuarioLogado.id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nome: nomeSessao }) 
      });
      setNomeSessao('');
      buscarMinhasSessoes();
      setTelaAtual('dashboard');
    } catch (erro) {
      alert("Erro ao criar sessão.");
    }
  };

  const deletarSessao = async (id, nome) => {
    if (!window.confirm(`Apagar a sessão "${nome}"?`)) return;
    try {
      await fetch(`${API_URL}/api/sessoes/${id}`, { method: 'DELETE' });
      buscarMinhasSessoes();
    } catch (erro) {
      alert("Erro ao apagar.");
    }
  };

  const buscarCategorias = async () => {
    try {
      const resposta = await fetch(`${API_URL}/api/categorias/sessao/${sessaoAtual.id}`);
      if (resposta.ok) {
        const lista = await resposta.json();
        setCategorias(lista);
        const inicial = {};
        lista.forEach(c => inicial[c.id] = 0);
        setNotasTemporarias(inicial);
      }
    } catch (erro) { console.error(erro); }
  };

  const salvarCategoria = async () => {
    try {
      const url = categoriaEditando 
        ? `${API_URL}/api/categorias/${categoriaEditando.id}`
        : `${API_URL}/api/categorias?sessaoId=${sessaoAtual.id}`;
      
      await fetch(url, {
        method: categoriaEditando ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nome: nomeCategoria, peso: parseFloat(pesoCategoria) })
      });
      setNomeCategoria('');
      setPesoCategoria(1.0);
      setCategoriaEditando(null);
      buscarCategorias();
    } catch (erro) { alert("Erro ao salvar tier."); }
  };

  const buscarMusicas = async () => {
    try {
      const resposta = await fetch(`${API_URL}/api/musicas/sessao/${sessaoAtual.id}`);
      if (resposta.ok) {
        const lista = await resposta.json();
        lista.sort((a, b) => (b.notaFinal || 0) - (a.notaFinal || 0));
        setMusicas(lista);
      }
    } catch (erro) { console.error(erro); }
  };

  const lidarComMudancaDeNota = (id, val) => {
    setNotasTemporarias({ ...notasTemporarias, [id]: parseInt(val) });
  };

  const salvarMusicaEAvaliacoes = async () => {
    if (!nomeMusicaNova || !artistaMusicaNova) return alert("Preencha música e artista!");
    try {
      // 1. Criar Música
      const resM = await fetch(`${API_URL}/api/musicas?sessaoId=${sessaoAtual.id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nome: nomeMusicaNova, artista: artistaMusicaNova })
      });
      const musicaSalva = await resM.json();

      // 2. Salvar Notas (Enviando IDs via URL para o Controller)
      for (let catId of Object.keys(notasTemporarias)) {
        const urlA = `${API_URL}/api/avaliacoes?musicaId=${musicaSalva.id}&categoriaId=${catId}`;
        await fetch(urlA, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ nota: notasTemporarias[catId] })
        });
      }

      // 3. Calcular Média
      await fetch(`${API_URL}/api/musicas/${musicaSalva.id}/calcular-nota`, { method: 'PUT' });

      // 4. Resetar e Atualizar
      setNomeMusicaNova('');
      setArtistaMusicaNova('');
      const reset = {};
      categorias.forEach(c => reset[c.id] = 0);
      setNotasTemporarias(reset);
      buscarMusicas();
    } catch (erro) { alert("Erro ao avaliar."); }
  };

  // ==========================================
  // 4. O ROSTO: INTERFACE
  // ==========================================
  return (
    <div className="min-h-screen bg-sky-200 p-4 md:p-8 font-sans text-gray-900 flex flex-col items-center justify-center">
      
      {telaAtual !== 'detalhesSessao' && (
        <div className="max-w-2xl w-full bg-white border-4 border-black rounded-3xl p-10 shadow-[8px_8px_0px_0px_rgba(0,0,0,1)] text-center">
          <h1 className="text-5xl font-black mb-6 uppercase tracking-wider text-pink-500" style={{ textShadow: '3px 3px 0px #000' }}>🎵 MusicTier</h1>

          {telaAtual === 'home' && (
            <button className="bg-yellow-400 font-bold text-lg py-4 px-8 border-4 border-black rounded-2xl shadow-[4px_4px_0px_0px_rgba(0,0,0,1)] active:translate-y-1" onClick={() => setTelaAtual('login')}>🚀 Começar</button>
          )}

          {telaAtual === 'login' && (
            <div className="flex flex-col items-center">
              <input type="text" placeholder="Nome DJ..." value={nomeUsuario} onChange={(e) => setNomeUsuario(e.target.value)} className="w-full border-4 border-black rounded-xl p-4 text-xl font-bold mb-6 shadow-[4px_4px_0px_0px_rgba(0,0,0,1)]"/>
              <div className="flex gap-4">
                <button className="bg-gray-300 font-bold py-3 px-6 border-4 border-black rounded-xl" onClick={() => setTelaAtual('home')}>Voltar</button>
                <button className="bg-green-400 font-bold py-3 px-8 border-4 border-black rounded-xl" onClick={fazerLogin}>Entrar 🎧</button>
              </div>
            </div>
          )}

          {telaAtual === 'dashboard' && (
            <div className="flex flex-col items-center">
              <button className="bg-yellow-400 font-bold py-4 px-8 border-4 border-black rounded-2xl mb-8" onClick={() => setTelaAtual('criarSessao')}>🎸 Criar Sessão</button>
              <div className="w-full text-left bg-gray-100 p-6 rounded-2xl border-4 border-black shadow-[4px_4px_0px_0px_rgba(0,0,0,1)]">
                <h3 className="text-2xl font-black mb-4 uppercase text-pink-500">Suas Sessões</h3>
                <div className="flex flex-col gap-3">
                  {minhasSessoes.map(s => (
                    <div key={s.id} className="flex justify-between items-center bg-white border-2 border-black p-4 rounded-xl">
                      <span className="font-bold">{s.nome}</span>
                      <div className="flex gap-2">
                        <button className="bg-red-400 font-bold py-1 px-3 border-2 border-black rounded-lg" onClick={() => deletarSessao(s.id, s.nome)}>🗑️</button>
                        <button className="bg-blue-400 font-bold py-1 px-4 border-2 border-black rounded-lg" onClick={() => { setSessaoAtual(s); setTelaAtual('detalhesSessao'); }}>Entrar</button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {telaAtual === 'criarSessao' && (
            <div className="flex flex-col items-center">
              <input type="text" placeholder="Nome..." value={nomeSessao} onChange={(e) => setNomeSessao(e.target.value)} className="w-full border-4 border-black rounded-xl p-4 text-xl font-bold mb-8 shadow-[4px_4px_0px_0px_rgba(0,0,0,1)]"/>
              <div className="flex gap-4">
                <button className="bg-gray-300 font-bold py-3 px-6 border-4 border-black rounded-xl" onClick={() => setTelaAtual('dashboard')}>Cancelar</button>
                <button className="bg-blue-400 font-bold py-3 px-8 border-4 border-black rounded-xl" onClick={criarNovaSessao}>Salvar 💾</button>
              </div>
            </div>
          )}
        </div>
      )}

      {telaAtual === 'detalhesSessao' && sessaoAtual && (
        <div className="w-full max-w-7xl bg-white border-4 border-black rounded-3xl p-10 shadow-[8px_8px_0px_0px_rgba(0,0,0,1)]">
          <div className="flex justify-between items-center mb-8 border-b-4 border-black pb-4">
            <button className="bg-gray-300 font-bold py-2 px-4 border-4 border-black rounded-xl" onClick={() => setTelaAtual('dashboard')}>⬅️ Voltar</button>
            <h2 className="text-4xl font-black uppercase text-purple-600">{sessaoAtual.nome}</h2>
            <div className="w-16"></div>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-5 gap-8">
            <div className="lg:col-span-3 flex flex-col gap-8">
              <div className="bg-yellow-100 p-6 rounded-2xl border-4 border-black">
                <h3 className="text-xl font-black mb-4 uppercase">1. Tiers e Pesos</h3>
                <div className="flex gap-2 items-end mb-4">
                  <input type="text" placeholder="Nome..." value={nomeCategoria} onChange={e => setNomeCategoria(e.target.value)} className="flex-1 border-4 border-black rounded-xl p-2 font-bold" />
                  <input type="number" value={pesoCategoria} onChange={e => setPesoCategoria(e.target.value)} className="w-20 border-4 border-black rounded-xl p-2 font-bold" />
                  <button className="bg-green-400 font-bold py-2 px-6 border-4 border-black rounded-xl" onClick={salvarCategoria}>{categoriaEditando ? '✏️' : '➕'}</button>
                </div>
                <div className="flex flex-wrap gap-2">
                  {categorias.map(c => (
                    <div key={c.id} onClick={() => { setCategoriaEditando(c); setNomeCategoria(c.nome); setPesoCategoria(c.peso); }} className="cursor-pointer bg-white border-2 border-black px-3 py-1 rounded-full font-bold">
                      {c.nome} <span className="bg-black text-white text-xs px-2 rounded">x{c.peso}</span>
                    </div>
                  ))}
                </div>
              </div>

              <div className="bg-pink-100 p-6 rounded-2xl border-4 border-black">
                <h3 className="text-xl font-black mb-4 uppercase">2. Avaliar Música</h3>
                <div className="flex gap-4 mb-4">
                  <input type="text" placeholder="Música..." value={nomeMusicaNova} onChange={e => setNomeMusicaNova(e.target.value)} className="flex-1 border-4 border-black rounded-xl p-3 font-bold" />
                  <input type="text" placeholder="Artista..." value={artistaMusicaNova} onChange={e => setArtistaMusicaNova(e.target.value)} className="flex-1 border-4 border-black rounded-xl p-3 font-bold" />
                </div>
                {categorias.map(c => (
                  <div key={c.id} className="bg-white p-3 border-2 border-black rounded-xl mb-3">
                    <div className="flex justify-between font-bold mb-1"><span>{c.nome}</span><span>{notasTemporarias[c.id] || 0}/10</span></div>
                    <input type="range" min="0" max="10" value={notasTemporarias[c.id] || 0} onChange={e => lidarComMudancaDeNota(c.id, e.target.value)} className="w-full accent-pink-500" />
                  </div>
                ))}
                <button className="w-full bg-pink-500 text-white font-black py-4 border-4 border-black rounded-xl" onClick={salvarMusicaEAvaliacoes}>SALVAR 🎸</button>
              </div>
            </div>

            <div className="lg:col-span-2 bg-white p-6 rounded-2xl border-4 border-black flex flex-col h-[600px] overflow-hidden">
              <h3 className="text-2xl font-black mb-4 text-center uppercase text-blue-500">Ranking</h3>
              <div className="flex-1 overflow-y-auto pr-2 flex flex-col gap-4">
                {musicas.map((m, i) => (
                  <div key={m.id} className={`flex items-center gap-3 p-3 border-4 border-black rounded-xl shadow-[4px_4px_0px_0px_rgba(0,0,0,1)] ${i === 0 ? 'bg-yellow-300' : 'bg-gray-100'}`}>
                    <div className="text-2xl font-black w-8">{i + 1}º</div>
                    <div className="flex-1">
                      <p className="font-black text-sm truncate">{m.nome}</p>
                      <p className="font-bold text-xs opacity-70 truncate">{m.artista}</p>
                    </div>
                    <div className="text-xl font-black bg-white border-2 border-black px-2 rounded-lg">{m.notaFinal ? m.notaFinal.toFixed(1) : '0.0'}</div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;