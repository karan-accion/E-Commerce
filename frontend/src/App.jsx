import React, { useState } from 'react'
import UserManagement from './components/UserManagement'
import InventoryManagement from './components/InventoryManagement'

export default function App() {
  const [view, setView] = useState('users')

  return (
    <div className="app">
      <h1>E-Commerce Admin UI</h1>
      <nav>
        <button onClick={() => setView('users')}>User Management</button>
        <button onClick={() => setView('inventory')}>Inventory Management</button>
      </nav>
      <main>
        {view === 'users' ? <UserManagement /> : <InventoryManagement />}
      </main>
    </div>
  )
}
