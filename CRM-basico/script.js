let contatos = [];
let totalMensagens = 0;

function showPage(id) {
  document.querySelectorAll("section").forEach(s => s.classList.remove("active"));
  document.getElementById(id).classList.add("active");
}

function addContato() {
  let nome = document.getElementById("nome");
  let telefone = document.getElementById("telefone");

  if (!nome.value || !telefone.value) return;

  contatos.push({ nome: nome.value, telefone: telefone.value });
  renderContatos();

  nome.value = "";
  telefone.value = "";
}

function renderContatos() {
  let lista = document.getElementById("listaContatos");
  let select = document.getElementById("contatoSelect");

  lista.innerHTML = "";
  select.innerHTML = "";

  contatos.forEach((c, i) => {
    let li = document.createElement("li");
    li.innerText = c.nome + " - " + c.telefone;
    lista.appendChild(li);

    let option = document.createElement("option");
    option.value = i;
    option.text = c.nome;
    select.appendChild(option);
  });

  document.getElementById("totalContatos").innerText = contatos.length;
}

function enviarMensagem() {
  let index = document.getElementById("contatoSelect").value;
  let mensagem = document.getElementById("mensagem");

  if (!mensagem.value || contatos.length === 0) return;

  let chat = document.getElementById("chat");

  let msg = document.createElement("p");
  msg.innerText = contatos[index].nome + ": " + mensagem.value;

  chat.appendChild(msg);

  totalMensagens++;
  document.getElementById("totalMensagens").innerText = totalMensagens;

  mensagem.value = "";
}
