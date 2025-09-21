import React, { useState } from 'react'
import { runSimulation } from '../api'


export default function SimulationForm() {
    const [initialX, setInitialX] = useState(0.1)
    const [initialY, setInitialY] = useState(0.1)
    const [steps, setSteps] = useState(200)
    const [timeStep, setTimeStep] = useState(0.05)
    const [running, setRunning] = useState(false)
    const [message, setMessage] = useState<string | null>(null)


    async function onRun(e: React.FormEvent) {
        e.preventDefault()
        setRunning(true)
        setMessage(null)
        try {
            await runSimulation({ initialX, initialY, steps, timeStep })
            setMessage('Simulation submitted')
        } catch (err) {
            setMessage('Failed: ' + String(err))
        } finally { setRunning(false) }
    }


    return (
        <form onSubmit={onRun} className="bg-white p-4 rounded shadow">
            <h2 className="font-semibold mb-2">Simulation</h2>
            <div className="grid grid-cols-2 gap-2">
                <label className="text-xs">Initial X<input type="number" step="0.01" value={initialX} onChange={e => setInitialX(Number(e.target.value))} className="mt-1 input" /></label>
                <label className="text-xs">Initial Y<input type="number" step="0.01" value={initialY} onChange={e => setInitialY(Number(e.target.value))} className="mt-1 input" /></label>
                <label className="text-xs">Steps<input type="number" value={steps} onChange={e => setSteps(Number(e.target.value))} className="mt-1 input" /></label>
                <label className="text-xs">Time step<input type="number" step="0.01" value={timeStep} onChange={e => setTimeStep(Number(e.target.value))} className="mt-1 input" /></label>
            </div>
            <div className="mt-3 flex items-center gap-2">
                <button type="submit" disabled={running} className="px-3 py-1 bg-sky-600 text-white rounded">{running ? 'Running...' : 'Run'}</button>
                <div className="text-sm text-slate-500">{message}</div>
            </div>
        </form>
    )
}