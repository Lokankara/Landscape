import { CellDto, Point3D, SimulationRequest, GridRequest, RefineRequest } from './types';

const BASE = '/api';

export async function fetchGridAttractors(request: GridRequest): Promise<number[][]> {
    const res = await fetch(`${BASE}/attractors/grid`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request),
    });
    return res.json();
}

export async function refineAttractors(request: RefineRequest): Promise<number[][]> {
    const res = await fetch(`${BASE}/attractors/refine`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request),
    });
    return res.json();
}

export async function runSimulation(request: SimulationRequest) {
    const res = await fetch(`${BASE}/simulation/run`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request),
    });
    return res.json();
}

export async function simulateTrajectory(request: SimulationRequest) {
    const res = await fetch(`${BASE}/simulation/trajectory`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request),
    });
    return res.json();
}

export async function simulateEnsemble(request: SimulationRequest) {
    const res = await fetch(`${BASE}/simulation/ensemble`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request),
    });
    return res.json();
}

export async function simulateEnsembleGRN(request: SimulationRequest) {
    const res = await fetch(`${BASE}/simulation/ensemble/grn`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request),
    });
    return res.json();
}

export async function computeGradient(request: { point: number[]; potentialPoints: number[][]; diffusionCoefficient: number }) {
    const res = await fetch(`${BASE}/simulation/gradient`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request),
    });
    return res.json();
}

export async function getSimulationStableStates() {
    const res = await fetch(`${BASE}/simulation/stable-states`);
    return res.json();
}

export async function simulateLandscape(request: SimulationRequest) {
    const res = await fetch(`${BASE}/landscape/simulate`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request),
    });
    return res.json();
}

export async function getLandscapeStableStates() {
    const res = await fetch(`${BASE}/landscape/stable-states`);
    return res.json();
}

export async function getAllLandscapeStates() {
    const res = await fetch(`${BASE}/landscape/states`);
    return res.json();
}

export async function searchLandscapeStates(query: string) {
    const res = await fetch(`${BASE}/landscape/states/search?query=${encodeURIComponent(query)}`);
    return res.json();
}

export async function getEpigeneticLandscape(): Promise<Point3D[]> {
    const res = await fetch(`${BASE}/epigenetic/landscape`);
    return res.json();
}

export async function simulateEpigenetic(initial: CellDto, steps: number, simulationRunId: number) {
    const res = await fetch(`${BASE}/epigenetic/simulate?steps=${steps}&simulationRunId=${simulationRunId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(initial),
    });
    return res.json();
}

export async function getEpigeneticStatesByType(type: string) {
    const res = await fetch(`${BASE}/epigenetic/states/${type}`);
    return res.json();
}
