
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
  const [plans, setPlans] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    fetch('http://localhost:8080/subscription/active/plans')
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error: ${response.status}`)
        }

        return response.json()
      })
      .then((data) => {
        setPlans(data)
      })
      .catch((error) => {
        console.error('Error fetching plans:', error)
        setError('Unable to load subscription plans.')
      })
      .finally(() => {
        setLoading(false)
      })
  }, [])

  return (
    <div>
      <h1>Subscription Plans</h1>

      {loading && <p>Loading plans...</p>}

      {error && <p>{error}</p>}

      {!loading && !error && plans.length === 0 && (
        <p>No subscription plans are currently available.</p>
      )}

      {!loading && !error && plans.map((plan) => (
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
