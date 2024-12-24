// scripts.js
function showPopup(popupId) {
    document.getElementById(popupId).style.display = "flex";
}

function hidePopup(popupId) {
    document.getElementById(popupId).style.display = "none";
}
function submitUpiPayment() {
    const upiId = document.querySelector(".input-box").value; // UPI ID
    const amount = 1; // Fixed amount (you can make it dynamic)

    if (!upiId) {
        alert("Please enter your UPI ID!");
        return;
    }

    // Create payment order through the backend
    fetch("http://localhost:8080/api/payment/upi", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ upiId, amount }),
    })
        .then((response) => response.json())
        .then((data) => {
            if (data.error) {
                throw new Error(data.error);
            }

            // Trigger Razorpay Checkout
            const options = {
                key: data.razorpayKey, // Key from backend
                amount: amount * 100, // Convert INR to paise
                currency: "INR",
                name: "Your Company Name", // Your business name
                description: "Test Transaction", // Description for the transaction
                order_id: data.orderId, // Order ID generated from backend
                handler: function (response) {
                    alert(
                        "Payment Successful! Payment ID: " + response.razorpay_payment_id
                    );
                },
                prefill: {
                    name: "Customer Name", // Prefill fields (optional)
                    email: "customer@example.com",
                    contact: "8357028350",
                },
                theme: {
                    color: "#3399cc", // Razorpay popup theme color
                },
            };

            const rzp = new Razorpay(options);
            rzp.open();
        })
        .catch((error) => {
            console.error("Error:", error.message);
            alert("Payment initiation failed: " + error.message);
        });
}
