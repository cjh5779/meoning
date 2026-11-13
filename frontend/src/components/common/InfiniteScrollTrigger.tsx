import { useEffect, useRef } from 'react'

type InfiniteScrollTriggerProps = {
    onIntersect: () => void
    disabled: boolean
}

export default function InfiniteScrollTrigger({ onIntersect, disabled }: InfiniteScrollTriggerProps) {
    const ref = useRef<HTMLDivElement>(null)

    useEffect(() => {
        const target = ref.current
        if (!target || disabled) {
            return
        }

        const observer = new IntersectionObserver(
            (entries) => {
                if (entries[0].isIntersecting) {
                    onIntersect()
                }
            },
            { rootMargin: '200px' },
        )
        observer.observe(target)

        return () => observer.disconnect()
    }, [onIntersect, disabled])

    return <div ref={ref} className="h-1" />
}