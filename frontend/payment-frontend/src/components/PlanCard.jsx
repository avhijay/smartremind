function PlanCard({ plan, onChoosePlan }) {
  return (
    <div>
      <h2>{plan.subscriptionPlan}</h2>
      <p>₹{plan.amount}</p>
      <p>{plan.planDurationDays} days</p>

      <button onClick={() => onChoosePlan(plan)}>
        Choose Plan
      </button>
    </div>
  )
}

export default PlanCard