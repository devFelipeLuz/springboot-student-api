import { BrowserRouter, Route, Routes } from 'react-router-dom'
import LoginPage from './pages/Login/LoginPage'
import { GlobalStyle } from './styles/global'
import DashboardPage from './pages/Dashboard/DashboardPage'

function App() {

  return (
    <BrowserRouter>
    <GlobalStyle />
      <Routes>
        <Route path='/' element={<LoginPage />} />
        <Route path='/dashboard' element={<DashboardPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
