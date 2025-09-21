export type Point3D = { x: number; y: number; z: number }
export type SimulationRequest = Partial<{ initialX: number; initialY: number; initialZ: number; steps: number; timeStep: number }>


export async function runSimulation(req: SimulationRequest) {
    const res = await fetch('/api/simulation/run', {
        method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(req)
    })
    if (!res.ok) throw new Error(String(res.status))
    return res.json()
}


export async function fetchLandscape(): Promise<Point3D[]> {
    const res = await fetch('/api/epigenetic/landscape')
    if (!res.ok) throw new Error(String(res.status))
    return res.json()
}


export async function findGridAttractors(payload: any) {
    const res = await fetch('/api/attractors/grid', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) })
    if (!res.ok) throw new Error(String(res.status))
    return res.json()
}