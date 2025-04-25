$(document).ready(function() {
    const form = document.getElementById('compraForm');
    const fornecedorSelect = document.getElementById('fornecedor');

    // Objeto com as alíquotas por estado
    const aliquotasPorEstado = {
        AC: 17.0, AL: 18.0, AM: 18.0, AP: 18.0, BA: 18.0,
        CE: 18.0, DF: 18.0, ES: 17.0, GO: 17.0, MA: 18.0,
        MG: 18.0, MS: 17.0, MT: 17.0, PA: 17.0, PB: 18.0,
        PE: 18.0, PI: 18.0, PR: 18.0, RJ: 20.0, RN: 18.0,
        RO: 17.5, RR: 17.0, RS: 18.0, SC: 17.0, SE: 18.0,
        SP: 18.0, TO: 17.0
    };

    // Alternar entre novo produto e produto existente
    $('input[name="produtoOption"]').change(function() {
        const isNovoProduto = $(this).val() === 'novo';
        $('#isNovoProduto').val(isNovoProduto);

        if (isNovoProduto) {
            $('#novoProdutoSection').removeClass('d-none');

            $('#produtoExistenteSection').addClass('d-none');

            $('#produtoExistente, #produtoExistentePrecoCompra, #produtoExistentePrecoVenda')
                .prop('required', false)
                .removeClass('is-invalid');

            $('#produto\\.nome, #produto\\.precoCompra, #produto\\.precoVenda')
                .prop('required', true);
        } else {
            $('#novoProdutoSection').addClass('d-none');

            $('#produtoExistenteSection').removeClass('d-none');

            $('#produto\\.nome, #produto\\.precoCompra, #produto\\.precoVenda')
                .prop('required', false)
                .removeClass('is-invalid');

            $('#produtoExistente, #produtoExistentePrecoCompra, #produtoExistentePrecoVenda')
                .prop('required', true);
        }

        calcularTotal();
    });

    // Preencher automaticamente os campos quando selecionar um produto existente
    $('#produtoExistente').change(function() {
        const selectedOption = $(this).find('option:selected');
        if (selectedOption.val()) {
            const precoCompra = selectedOption.data('preco-compra');
            const precoVenda = selectedOption.data('preco-venda');
            const nomeProduto = selectedOption.data('nome');

            $('#produtoExistentePrecoCompra').val(precoCompra);
            $('#produtoExistentePrecoVenda').val(precoVenda);
            $('#produtoExistenteNomeHidden').val(nomeProduto);

            $('#produtoExistentePrecoCompraHidden').val(precoCompra);
            $('#produtoExistentePrecoVendaHidden').val(precoVenda);

            $(this).removeClass('is-invalid').next('.invalid-feedback').hide();
        }
        calcularTotal();
    });

    // Atualiza os campos hidden quando os preços são editados
    $('#produtoExistentePrecoCompra, #produtoExistentePrecoVenda').on('input', function() {
        $('#produtoExistentePrecoCompraHidden').val($('#produtoExistentePrecoCompra').val());
        $('#produtoExistentePrecoVendaHidden').val($('#produtoExistentePrecoVenda').val());
        calcularTotal();
    });

    // Validação antes do submit
    form.addEventListener('submit', function(e) {
        e.preventDefault();

        // Atualiza os campos hidden com os valores atuais
        if (!$('#novoProduto').is(':checked')) {
            $('#produtoExistentePrecoCompraHidden').val($('#produtoExistentePrecoCompra').val());
            $('#produtoExistentePrecoVendaHidden').val($('#produtoExistentePrecoVenda').val());
        }

        let isValid = true;
        const isNovoProduto = $('input[name="produtoOption"]:checked').val() === 'novo';

        // Validar campos comuns
        $('#fornecedor, #dataCompra, #quantidade').each(function() {
            if (!this.value) {
                $(this).addClass('is-invalid').next('.invalid-feedback').show();
                isValid = false;
            } else {
                $(this).removeClass('is-invalid').next('.invalid-feedback').hide();
            }
        });

        // Validar seção de produto
        if (isNovoProduto) {
            $('#produto\\.nome, #produto\\.precoCompra, #produto\\.precoVenda').each(function() {
                if (!this.value) {
                    $(this).addClass('is-invalid').next('.invalid-feedback').show();
                    isValid = false;
                } else {
                    $(this).removeClass('is-invalid').next('.invalid-feedback').hide();
                }
            });
        } else {
            $('#produtoExistente, #produtoExistentePrecoCompra, #produtoExistentePrecoVenda').each(function() {
                if (!this.value) {
                    $(this).addClass('is-invalid').next('.invalid-feedback').show();
                    isValid = false;
                } else {
                    $(this).removeClass('is-invalid').next('.invalid-feedback').hide();
                }
            });
        }

        if (isValid) {
            form.submit();
        } else {
            $('html, body').animate({
                scrollTop: $('.is-invalid').first().offset().top - 100
            }, 500);
        }
    });

    function getAliquotaFornecedor() {
        const selectedOption = fornecedorSelect.options[fornecedorSelect.selectedIndex];
        if (selectedOption && selectedOption.value) {
            const estado = selectedOption.getAttribute('data-estado');
            return aliquotasPorEstado[estado] || 17.0;
        }
        return 17.0;
    }

    function calcularTotal() {
        const isNovoProduto = $('input[name="produtoOption"]:checked').val() === 'novo';
        let preco;

        if (isNovoProduto) {
            preco = parseFloat($('#produto\\.precoCompra').val()) || 0;
        } else {
            const precoEditado = parseFloat($('#produtoExistentePrecoCompra').val());
            const precoOriginal = parseFloat($('#produtoExistente').find('option:selected').data('preco-compra')) || 0;
            preco = isNaN(precoEditado) ? precoOriginal : precoEditado;
        }

        const quantidade = parseFloat($('#quantidade').val()) || 0;
        const aliquota = getAliquotaFornecedor();
        const icmsPercentual = aliquota / 100;
        const valorBase = preco * quantidade;
        const valorICMS = valorBase * icmsPercentual;
        const totalComICMS = valorBase + valorICMS;

        // Atualiza os campos
        $('#icms-percentual').val(aliquota.toFixed(2));
        $('#total-display').val(totalComICMS.toFixed(2));
        $('#total').val(valorBase.toFixed(2));
        $('#icms-display').val(valorICMS.toFixed(2));
        $('#valor-icms').val(valorICMS.toFixed(2));
    }

    // Atualiza o cálculo quando qualquer campo relevante mudar
    $('#produto\\.precoCompra, #produtoExistentePrecoCompra, #quantidade, #fornecedor').on('input change', calcularTotal);

    // Dispara o cálculo inicial
    calcularTotal();
});
