import type {AstralPosition} from '../types/AstralPosition';

const API_URL = 'http://localhost:8080/astralpositions';

export const fetchAllPositions = async (): Promise<AstralPosition[]> => {
    const response = await fetch(API_URL);
    return response.json();
};

export const fetchPositionById = async (id: string): Promise<AstralPosition> => {
    const response = await fetch(`${API_URL}/${id}`);
    return response.json();
};

export const createPosition = async (position: Omit<AstralPosition, 'id'>): Promise<AstralPosition> => {
    const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(position)
    });
    return response.json();
};

export const stepToEpoch = async (epoch: string): Promise<AstralPosition[]> => {
    const response = await fetch(`${API_URL}/stepto/${epoch}`);
    return response.json();
};

// Add update/delete functions as needed