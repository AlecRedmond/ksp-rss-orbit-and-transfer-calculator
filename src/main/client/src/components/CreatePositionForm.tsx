import React, { useState } from 'react';
import type {AstralPosition} from '../types/AstralPosition';
import { createPosition } from '../services/AstralPositionService';

const CreatePositionForm: React.FC = () => {
    const [formData, setFormData] = useState<Omit<AstralPosition, 'id'>>({
        body: '',
        timestamp: new Date().toISOString().slice(0, 16),
        xpos: 0,
        ypos: 0,
        zpos: 0
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await createPosition(formData);
            // Reset form or refresh data
            setFormData({
                body: '',
                timestamp: new Date().toISOString(),
                xpos: 0,
                ypos: 0,
                zpos: 0
            });
        } catch (error) {
            console.error('Error creating position:', error);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name.includes('Pos') ? parseFloat(value) : value
        }));
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Create New Position</h2>
            <label>
                Celestial Body:
                <input
                    type="text"
                    name="body"
                    value={formData.body}
                    onChange={handleChange}
                    required
                />
            </label>

            <label>
                Timestamp:
                <input
                    type="datetime-local"
                    name="timestamp"
                    value={formData.timestamp}
                    onChange={handleChange}
                    required
                />
            </label>

            <label>
                X Position:
                <input
                    type="number"
                    name="xPos"
                    value={formData.xpos}
                    onChange={handleChange}
                    step="0.01"
                    required
                />
            </label>

            <label>
                Y Position:
                <input
                    type="number"
                    name="yPos"
                    value={formData.ypos}
                    onChange={handleChange}
                    step="0.01"
                    required
                />
            </label>

            <label>
                Z Position:
                <input
                    type="number"
                    name="zPos"
                    value={formData.zpos}
                    onChange={handleChange}
                    step="0.01"
                    required
                />
            </label>

            <button type="submit">Create Position</button>
        </form>
    );
};

export default CreatePositionForm;