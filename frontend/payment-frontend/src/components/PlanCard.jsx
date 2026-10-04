
function PlanCard({ plan }) {
  return (
    <div>
      <h2>{plan.subscriptionPlan}</h2>

      <p>₹{plan.amount}</p>

      <p>{plan.planDurationDays} days</p>

      <button>Choose Plan</button>
    </div>
  )
}

export default PlanCard