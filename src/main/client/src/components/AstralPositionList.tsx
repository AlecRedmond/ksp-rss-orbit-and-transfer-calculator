import React, { useEffect, useState } from 'react';
import type {AstralPosition} from '../types/AstralPosition';
import { fetchAllPositions, stepToEpoch } from '../services/AstralPositionService';

const AstralPositionList: React.FC = () => {
    const [positions, setPositions] = useState<AstralPosition[]>([]);
    const [epoch, setEpoch] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        loadPositions();
    }, []);

    const loadPositions = async () => {
        setLoading(true);
        try {
            const data = await fetchAllPositions();
            setPositions(data);
        } catch (error) {
            console.error('Error fetching positions:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleEpochChange = async () => {
        if (!epoch) return;
        setLoading(true);
        try {
            const newPositions = await stepToEpoch(epoch + ":00.00Z");
            setPositions(newPositions);
        } catch (error) {
            console.error('Error stepping to epoch:', error);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <h1>Astral Positions</h1>

            {/* Epoch Control */}
            <div className="epoch-control">
                <input
                    type="datetime-local"
                    value={epoch}
                    onChange={(e) => setEpoch(e.target.value)}
                />
                <button onClick={handleEpochChange}>Step to Epoch</button>
            </div>

            {/* Positions Table */}
            <table>
                <thead>
                <tr>
                    <th>Body</th>
                    <th>Timestamp</th>
                    <th>X Position</th>
                    <th>Y Position</th>
                    <th>Z Position</th>
                </tr>
                </thead>
                <tbody>
                {positions.map(({body, id, timestamp, xpos, ypos, zpos}) => (
                    <tr key={id}>
                        <td>{body}</td>
                        <td>{new Date(timestamp).toLocaleString()}</td>
                        <td>{xpos.toFixed(2)}</td>
                        <td>{ypos.toFixed(2)}</td>
                        <td>{zpos.toFixed(2)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default AstralPositionList;