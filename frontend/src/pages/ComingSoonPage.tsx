type ComingSoonPageProps = {
    title: string
}

export default function ComingSoonPage({ title }: ComingSoonPageProps) {
    return (
        <main className="px-4 py-5">
            <h1 className="text-xl font-bold">{title}</h1>
            <p className="mt-2 text-sm text-gray-500">준비 중인 화면이에요.</p>
        </main>
    )
}