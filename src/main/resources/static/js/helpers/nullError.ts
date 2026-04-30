export default function throwNullReferenceError(message?: string): never {
    throw new ReferenceError(message)
}