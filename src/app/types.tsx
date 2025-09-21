export interface CellDto {
    id: number;
    geneExpression: number[];
    potentialGradient: number[];
    type: string;
}

export interface Point3D {
    x: number;
    y: number;
    z: number;
}

export interface RefineRequest {
    seeds: number[][];
    fullDim: number;
    tolerance: number;
    maxIter: number;
}

export interface GridRequest {
    potentialGrid: number[][][];
    xGrid: number[];
    yGrid: number[];
    zGrid: number[];
}

export interface SimulationRequest {
    burnIn?: number;
    delta: number;
    diffusionCoefficient: number;
    bins?: number;
    sampleLimit: number;
    start: number[];
    nodeA?: number[];
    nodeB?: number[];
    initialX?: number;
    initialY?: number;
    initialZ?: number;
    steps?: number;
    ensembleRuns?: number;
    timeStep?: number;
    includeStochasticity?: boolean;
}
