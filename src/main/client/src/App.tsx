import React from 'react';
import AstralPositionList from './components/AstralPositionList';
import CreatePositionForm from './components/CreatePositionForm';
import './App.css';

function App() {
    return (
        <div className="app">
            <header>
                <h1>Orbit Calculator</h1>
            </header>
            <main>
                <CreatePositionForm />
                <AstralPositionList />
            </main>
        </div>
    );
}

export default App;