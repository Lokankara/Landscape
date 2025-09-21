import React, {useState} from 'react';
import {BrowserRouter as Router, Link, Navigate, Route, Routes} from 'react-router-dom';
import {AppBar, Box, Tab, Tabs} from '@mui/material';
import AttractorsTab from './app/components/AttractorsTab';
import SimulationTab from './app/components/SimulationTab';
import LandscapeTab from './app/components/LandscapeTab';
import EpigeneticTab from './app/components/EpigeneticTab';

const App: React.FC = () => {
    const [tabIndex, setTabIndex] = useState<number>(0);

    const handleChange = (_: React.SyntheticEvent, newValue: number) => {
        setTabIndex(newValue);
    };

    return (
        <Router>
            <AppBar position="static">
                <Tabs value={tabIndex} onChange={handleChange} variant="fullWidth">
                    <Tab label="Attractors" component={Link} to="/attractors"/>
                    <Tab label="Simulation" component={Link} to="/simulation"/>
                    <Tab label="Landscape" component={Link} to="/landscape"/>
                    <Tab label="Epigenetic" component={Link} to="/epigenetic"/>
                </Tabs>
            </AppBar>
            <Box sx={{p: 2}}>
                <Routes>
                    <Route path="/" element={<Navigate to="/attractors" replace/>}/>
                    <Route path="/attractors" element={<AttractorsTab/>}/>
                    <Route path="/simulation" element={<SimulationTab/>}/>
                    <Route path="/landscape" element={<LandscapeTab/>}/>
                    <Route path="/epigenetic" element={<EpigeneticTab/>}/>
                </Routes>
            </Box>
        </Router>
    );
};

export default App;
