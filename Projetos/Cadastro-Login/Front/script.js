const formulario = document.querySelector("form");
const Inome = document.querySelector(".nome");
const Iemail = document.querySelector(".email");
const Isenha = document.querySelector(".senha");
const Itel = document.querySelector(".tel");

function cadastrar() {
    const usuario = {
        nome: Inome.value.trim(),
        email: Iemail.value.trim(),
        senha: Isenha.value.trim(),
        telefone: Itel.value.trim()
    };

    // Verifica se todos os campos foram preenchidos
    if (!usuario.nome || !usuario.email || !usuario.senha || !usuario.telefone) {
        alert('Todos os campos devem ser preenchidos!');
        return;
    }

    fetch("http://localhost:8080/usuarios", {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: "POST",
        body: JSON.stringify(usuario)
    })
    .then(function (res) {
        if (res.ok) {
            alert('Usuário cadastrado com sucesso!');
            limpar();  // Limpa os campos após o cadastro bem-sucedido
        } else {
            alert('Erro ao cadastrar o usuário!');
        }
    })
    .catch(function (error) {
        console.error("Erro ao enviar a requisição:", error);
    });
}

function limpar() {
    Inome.value = "";
    Iemail.value = "";
    Isenha.value = "";
    Itel.value = "";
}

formulario.addEventListener('submit', function(event) {
    event.preventDefault();
    cadastrar();
});
