import React, {useEffect, useState} from 'react';
import {getEpigeneticLandscape, getEpigeneticStatesByType, simulateEpigenetic} from '../api';
import {CellDto, Point3D} from '../types';

const EpigeneticTab: React.FC = () => {
    const [landscape, setLandscape] = useState<Point3D[]>([]);
    const [states, setStates] = useState<CellDto[]>([]);

    useEffect(() => {
        const load = async () => {
            const data = await getEpigeneticLandscape();
            setLandscape(data);
            const stateData = await getEpigeneticStatesByType('SOME_TYPE');
            setStates(stateData);
        };
        load();
    }, []);

    const handleSimulate = async () => {
        const initial: CellDto = {
            id: 0,
            geneExpression: [0.1, 0.2],
            potentialGradient: [0.0, 0.0],
            type: 'SOME_TYPE',
        };
        const result = await simulateEpigenetic(initial, 10, 1);
        console.log(result);
    };

    return (
        <div>
            <h2>Epigenetic Landscape</h2>
            <pre>{JSON.stringify(landscape, null, 2)}</pre>
            <h3>States</h3>
            <pre>{JSON.stringify(states, null, 2)}</pre>
            <button onClick={handleSimulate}>Simulate</button>
        </div>
    );
};

export default EpigeneticTab;
