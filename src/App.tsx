import React from 'react'
import SimulationForm from './app/components/SimulationForm'
import AttractorsForm from './app/components/AttractorsForm'
import LandscapeView from './app/components/LandscapeView'


export default function App() {
  return (
    <div className="min-h-screen bg-slate-50 text-slate-900 p-6">
      <header className="max-w-6xl mx-auto mb-6">
        <h1 className="text-2xl font-semibold">Epigenetic Landscape</h1>
        <p className="text-sm text-slate-600">Interactive UI for simulation, landscape and attractor analysis</p>
      </header>


      <main className="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="col-span-1 space-y-6">
          <SimulationForm />
          <AttractorsForm />
        </div>
        <div className="col-span-2">
          <LandscapeView />
        </div>
      </main>
    </div>
  )
}