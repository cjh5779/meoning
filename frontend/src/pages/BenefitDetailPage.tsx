import { useParams } from 'react-router'

export default function BenefitDetailPage() {
    const { benefitId } = useParams()

    return (
        <main className="mx-auto min-h-screen max-w-md bg-white px-4 py-5">
            <p>혜택 상세 #{benefitId}</p>
        </main>
    )
}