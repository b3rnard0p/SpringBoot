
document.addEventListener('DOMContentLoaded', function() {
    const produtoSelect = document.getElementById('produtoId');
    const quantidadeInput = document.getElementById('quantidade');
    const precoUnitarioInput = document.getElementById('precoUnitario');
    const totalDisplay = document.getElementById('total-display');
    const totalHidden = document.getElementById('total');

    function calcularTotal() {
        if (produtoSelect.value && quantidadeInput.value) {
            const precoText = produtoSelect.options[produtoSelect.selectedIndex].text;
            const preco = parseFloat(precoText.split('R$ ')[1]);
            const quantidade = parseInt(quantidadeInput.value);
            const total = (preco * quantidade).toFixed(2);

            precoUnitarioInput.value = preco.toFixed(2);
            totalDisplay.value = total;
            totalHidden.value = total;
        }
    }

    produtoSelect.addEventListener('change', function() {
        if (produtoSelect.value) {
            const precoText = produtoSelect.options[produtoSelect.selectedIndex].text;
            const preco = parseFloat(precoText.split('R$ ')[1]);
            precoUnitarioInput.value = preco.toFixed(2);
            calcularTotal();
        }
    });

    quantidadeInput.addEventListener('input', calcularTotal);
});