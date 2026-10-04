import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import { useState, useEffect } from 'react'
import PlanCard from './components/PlanCard'


function PaymentHome() {
  return (
    <div>
      <h1>Payment Home</h1>

      <Link to="/plans">Purchase a Plan</Link>
      <br />
      <Link to="/my-plan">View My Plan</Link>
    </div>
  )
}





function PlansPage() {
  

const [plans, setPlans] = useState([



  
  {
    id: 1,
    subscriptionPlan: 'MONTHLY',
    amount: 499,
    planDurationDays: 30,
    planIsActive: true,
  },
  {
    id: 2,
    subscriptionPlan: 'YEARLY',
    amount: 4999,
    planDurationDays: 365,
    planIsActive: true,
  },
])

useEffect(() => {
  console.log('PlansPage loaded')
}, [])

  return (
    <div>
      <h1>Subscription Plans</h1>

      {plans.map((plan) => (
  <PlanCard key={plan.id} plan={plan} />
))}


    </div>
  )
}




function MyPlanPage() {
  return <h1>My Plan Page</h1>
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<PaymentHome />} />
        <Route path="/plans" element={<PlansPage />} />
        <Route path="/my-plan" element={<MyPlanPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App