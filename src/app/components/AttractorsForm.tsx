import React, { useState } from 'react'
import { findGridAttractors } from '../api'


export default function AttractorsForm() {
    const [status, setStatus] = useState<string | null>(null)


    async function onFind(e: React.FormEvent) {
        e.preventDefault()
        setStatus(null)
        try {
            const res = await findGridAttractors({})
            setStatus('Found ' + (Array.isArray(res) ? res.length : 0) + ' attractors')
        } catch (err) {
            setStatus('Error: ' + String(err))
        }
    }


    return (
        <form onSubmit={onFind} className="bg-white p-4 rounded shadow">
            <h2 className="font-semibold mb-2">Attractors</h2>
            <div className="space-y-2">
                <button className="px-3 py-1 bg-emerald-600 text-white rounded" type="submit">Find from Grid</button>
                <div className="text-sm text-slate-500">{status}</div>
            </div>
        </form>
    )
}