// to remember the plan user selected
let selectedPlanId = null
 const currUserElement =   document.getElementById("current-user");

 const subscriptionPlanContainer = document.getElementById("plans-container");



const purchaseButton = document.getElementById("confirm-payment");
const paymentMethod = document.getElementById("payment-method");
const paymentStatus = document.getElementById("payment-status");



  function initializePage(){
   loadCurrentUser();
   loadSubPlans();



}

// get current user
  async function loadCurrentUser(){

try{

const response = await fetch("http://localhost:8080/subscription/current/user");

 if  (!response.ok){

      throw new Error("Unable to fetch current User")

}

 const  data = await response.json();


  currUserElement.textContent = data.username;




}  catch(error) {

    console.log(error);


}


}

// get sub plans
  async function loadSubPlans(){

  try {

  const response = await  fetch ("http://localhost:8080/subscription/active/plans");

  if (!response.ok){

    throw new Error("Unable to fetch subscription plans")

  }

  const planData = await response.json();

//display plans

for (const plan of planData ){

const   planCard = document.createElement("div");
planCard.classList.add("plan-card");



planCard.innerHTML = `

<h3> ${plan.subscriptionPlan}</h3>
<p>₹${plan.amount}</p>
<p>${plan.planDurationDays} Days</p>
<button data-plan-id="${plan.id}">
    Select
</button>

`;
const selectedButton = planCard.querySelector("button");

selectedButton.addEventListener("click" , function(){
selectedPlanId = plan.id;

});


subscriptionPlanContainer.appendChild(planCard);

}




  }catch(error){
  console.log(error);


  }


  }







  initializePage();



  // below this is generated refer after configuring the backend

  // Purchase Subscription
purchaseButton.addEventListener("click", purchaseSubscription);

async function purchaseSubscription() {

    if (selectedPlanId === null) {
        alert("Please select a subscription plan.");
        return;
    }

    const paymentRequest = {
        planId: selectedPlanId,
        paymentMethod: paymentMethod.value,
        idempotencyKey: "test-key"
    };

    try {

        paymentStatus.textContent = "Processing...";

        const response = await fetch("http://localhost:8080/payment/subscribe", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(paymentRequest)
        });

        if (!response.ok) {
            throw new Error("Payment failed.");
        }

        const paymentResponse = await response.json();

        paymentStatus.textContent = "Payment Successful";

        console.log(paymentResponse);

    } catch (error) {

        paymentStatus.textContent = "Payment Failed";

        console.error(error);

    }

}