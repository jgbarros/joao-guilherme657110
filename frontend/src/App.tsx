import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { Toast } from 'primereact/toast'
import { toastRef } from './utils/toastService'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import ArtistList from './pages/ArtistList'
import AlbumList from './pages/AlbumList'

function App() {
  return (
    <BrowserRouter>
      <Toast ref={toastRef} />
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/artistas" element={<ArtistList />} />
        <Route path="/albuns" element={<AlbumList />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
