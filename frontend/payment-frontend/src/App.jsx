import { BrowserRouter, Routes, Route ,Link } from 'react-router-dom'



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
  return <h1>Plans Page</h1>
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