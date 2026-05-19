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

    var addToCartForms = document.querySelectorAll(".js-add-to-cart-form");
    var cartBadges = document.querySelectorAll(".js-cart-count");

    if (cartBadges.length === 0) {
        cartBadges = document.querySelectorAll(".cart-badge");
    }

    function updateCartBadges(count) {
        cartBadges.forEach(function (badge) {
            badge.textContent = String(count);
        });
    }

    addToCartForms.forEach(function (form) {
        var button = form.querySelector("button[type='submit']");
        if (button) {
            button.dataset.originalHtml = button.innerHTML;
        }

        form.addEventListener("submit", function (event) {
            event.preventDefault();

            var body = new URLSearchParams(new FormData(form)).toString();
            var ajaxAction = form.getAttribute("data-ajax-action") || form.action;
            if (button) {
                button.disabled = true;
                button.innerHTML = '<i class="fa-solid fa-spinner fa-spin me-2"></i> Adding...';
            }

            fetch(ajaxAction, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
                },
                body: body
            })
                .then(function (response) {
                    return response.json();
                })
                .then(function (data) {
                    if (!data || data.success !== true) {
                        if (data && data.redirectUrl) {
                            window.location.href = data.redirectUrl;
                            return;
                        }
                        throw new Error("Add to cart failed");
                    }

                    if (typeof data.cartCount !== "undefined") {
                        updateCartBadges(data.cartCount);
                    }

                    if (button) {
                        button.innerHTML = '<i class="fa-solid fa-circle-check me-2"></i> Added';
                        window.setTimeout(function () {
                            button.innerHTML = button.dataset.originalHtml;
                        }, 1000);
                    }
                })
                .catch(function () {
                    if (button) {
                        button.innerHTML = button.dataset.originalHtml;
                    }
                })
                .finally(function () {
                    if (button) {
                        window.setTimeout(function () {
                            button.disabled = false;
                        }, 1000);
                    }
                });
        });
    });
});
