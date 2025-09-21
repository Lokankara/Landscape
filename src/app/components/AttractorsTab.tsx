import React, {useEffect, useState} from 'react';
import {fetchGridAttractors, refineAttractors} from '../api';
import {GridRequest, RefineRequest} from '../types';

const AttractorsTab: React.FC = () => {
    const [grid, setGrid] = useState<number[][]>([]);
    const [refined, setRefined] = useState<number[][]>([]);

    useEffect(() => {
        const loadGrid = async () => {
            const request: GridRequest = {
                potentialGrid: [[[0.1]]],
                xGrid: [0],
                yGrid: [0],
                zGrid: [0],
            };
            const data = await fetchGridAttractors(request);
            setGrid(data);
        };
        loadGrid();
    }, []);

    const handleRefine = async () => {
        const request: RefineRequest = {
            seeds: grid,
            fullDim: 3,
            tolerance: 1e-6,
            maxIter: 50,
        };
        const data = await refineAttractors(request);
        setRefined(data);
    };

    return (
        <div>
            <h2>Grid Attractors</h2>
            <pre>{JSON.stringify(grid, null, 2)}</pre>
            <button onClick={handleRefine}>Refine</button>
            <h3>Refined Attractors</h3>
            <pre>{JSON.stringify(refined, null, 2)}</pre>
        </div>
    );
};

export default AttractorsTab;
