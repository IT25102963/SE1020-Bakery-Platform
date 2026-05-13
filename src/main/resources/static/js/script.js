document.addEventListener("DOMContentLoaded", function () {
    var confirmLinks = document.querySelectorAll(".js-confirm");

    confirmLinks.forEach(function (link) {
        link.addEventListener("click", function (event) {
            var message = link.getAttribute("data-confirm-message") || "Are you sure?";
            if (!window.confirm(message)) {
                event.preventDefault();
            }
        });
    });

    var cakeInput = document.querySelector(".js-cake-name");
    var qtyInput = document.querySelector(".js-quantity");
    var totalInput = document.querySelector(".js-total-price");

    function updateTotalPrice() {
        if (!cakeInput || !qtyInput || !totalInput) {
            return;
        }

        var selectedOption = cakeInput.options[cakeInput.selectedIndex];
        var unitPrice = selectedOption ? parseFloat(selectedOption.getAttribute("data-price") || "0") : 0;
        var quantity = parseInt(qtyInput.value || "0", 10);

        if (Number.isNaN(quantity) || quantity < 0) {
            quantity = 0;
        }

        var total = unitPrice * quantity;
        totalInput.value = total.toFixed(2);
    }

    if (cakeInput && qtyInput && totalInput) {
        cakeInput.addEventListener("change", updateTotalPrice);
        qtyInput.addEventListener("input", updateTotalPrice);
        updateTotalPrice();
    }
});
