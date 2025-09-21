import React, {useEffect, useState} from 'react';
import {getAllLandscapeStates} from '../api';
import {CellDto} from '../types';

const LandscapeTab: React.FC = () => {
    const [states, setStates] = useState<CellDto[]>([]);

    useEffect(() => {
        const loadStates = async () => {
            const data = await getAllLandscapeStates();
            setStates(data);
        };
        loadStates();
    }, []);

    return (
        <div>
            <h2>Landscape States</h2>
            <pre>{JSON.stringify(states, null, 2)}</pre>
        </div>
    );
};

export default LandscapeTab;
