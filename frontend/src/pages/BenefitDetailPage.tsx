import { Link, useLocation, useNavigate, useParams } from 'react-router'
import CategoryBadge from '../components/benefit/CategoryBadge'
import DdayBadge from '../components/benefit/DdayBadge'
import { getApiErrorCode } from '../api/error'
import { useBenefitDetail } from '../hooks/useBenefitDetail'
import { formatPeriod } from '../utils/date'
import { formatTargetCondition } from '../utils/targetCondition'

type InfoRowProps = {
    label: string
    value: string
}

function InfoRow({ label, value }: InfoRowProps) {
    return (
        <div className="flex gap-4 py-2 text-sm">
            <dt className="w-16 shrink-0 text-gray-500">{label}</dt>
            <dd className="whitespace-pre-line text-gray-900">{value}</dd>
        </div>
    )
}

export default function BenefitDetailPage() {
    const { benefitId } = useParams()
    const navigate = useNavigate()
    const location = useLocation()
    const { data, isPending, isError, error } = useBenefitDetail(Number(benefitId))

    const goBack = () => {
        if (location.key === 'default') {
            navigate('/')
        } else {
            navigate(-1)
        }
    }

    const notFound =
        !Number.isInteger(Number(benefitId)) ||
        (isError && getApiErrorCode(error) === 'BENEFIT_NOT_FOUND')

    return (
        <div className="mx-auto min-h-screen max-w-md bg-white pb-24">
            <header className="flex h-12 items-center px-2">
                <button type="button" onClick={goBack} className="px-2 text-2xl" aria-label="뒤로 가기">
                    ‹
                </button>
            </header>

            {notFound && (
                <div className="px-4 py-20 text-center">
                    <p className="text-sm text-gray-500">혜택을 찾을 수 없어요.</p>
                    <Link to="/" className="mt-4 inline-block rounded-full border border-gray-300 px-4 py-1.5 text-sm">
                        홈으로
                    </Link>
                </div>
            )}

            {!notFound && isPending && (
                <p className="py-20 text-center text-sm text-gray-400">불러오는 중...</p>
            )}

            {!notFound && isError && (
                <p className="py-20 text-center text-sm text-gray-500">혜택을 불러오지 못했어요.</p>
            )}

            {data && (
                <>
                    <div className="aspect-[4/3] bg-gray-100">
                        {data.thumbnailUrl && (
                            <img src={data.thumbnailUrl} alt="" className="h-full w-full object-cover" />
                        )}
                    </div>

                    <main className="px-4 py-5">
                        <div className="flex items-center gap-1.5">
                            <CategoryBadge category={data.category} />
                            <DdayBadge dDay={data.dDay} deadlineSoon={data.deadlineSoon} />
                        </div>
                        <h1 className="mt-2 text-xl font-bold leading-snug">{data.title}</h1>
                        <p className="mt-2 text-sm text-gray-600">{data.summary}</p>

                        <dl className="mt-5 rounded-xl bg-gray-50 px-4 py-2">
                            <InfoRow label="기간" value={formatPeriod(data.startDate, data.endDate)} />
                            <InfoRow label="대상" value={formatTargetCondition(data.targetCondition)} />
                            {data.applyMethod && <InfoRow label="신청 방법" value={data.applyMethod} />}
                        </dl>

                        {data.instaUrl && (
                            <a
                                href={data.instaUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="mt-4 inline-block text-sm text-gray-700"
                            >
                                인스타 게시물 보기 ›
                            </a>
                        )}
                    </main>

                    <div className="fixed bottom-0 left-1/2 w-full max-w-md -translate-x-1/2 border-t border-gray-200 bg-white p-3">
                        {data.expired ? (
                            <button
                                type="button"
                                disabled
                                className="h-12 w-full rounded-xl bg-gray-200 text-sm font-bold text-gray-500"
                            >
                                마감된 혜택이에요
                            </button>
                        ) : (
                            <a
                                href={data.officialUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="flex h-12 w-full items-center justify-center rounded-xl bg-gray-900 text-sm font-bold text-white"
                            >
                                공식 페이지에서 신청하기
                            </a>
                        )}
                    </div>
                </>
            )}
        </div>
    )
}