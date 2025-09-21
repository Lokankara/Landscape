import React, {useState} from 'react';
import {runSimulation, simulateTrajectory} from '../api';
import {SimulationRequest} from '../types';

const SimulationTab: React.FC = () => {
    const [trajectory, setTrajectory] = useState<number[][]>([]);

    const handleRun = async () => {
        const request: SimulationRequest = {
            start: [0.1, 0.2, 0.3],
            steps: 10,
            timeStep: 0.1,
            diffusionCoefficient: 0.05,
            delta: 0.1,
            sampleLimit: 10,
            ensembleRuns: 1,
            burnIn: 0,
            bins: 0,
        };
        await runSimulation(request);
        const traj = await simulateTrajectory(request);
        setTrajectory(traj);
    };

    return (
        <div>
            <h2>Simulation</h2>
            <button onClick={handleRun}>Run Simulation</button>
            <pre>{JSON.stringify(trajectory, null, 2)}</pre>
        </div>
    );
};

export default SimulationTab;
