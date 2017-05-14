export declare class Transformer {
    constructor(xsltPath: string | string[]);
    transform(input: DocumentFile): DocumentFile[];
}

export interface DocumentFile {
    base: string;
    path: string;
    contents: string;
}
