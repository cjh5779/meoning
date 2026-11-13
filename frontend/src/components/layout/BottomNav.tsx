import { NavLink } from 'react-router'

const tabs = [
    { to: '/', label: '홈', end: true },
    { to: '/calendar', label: '캘린더' },
    { to: '/bookmarks', label: '찜' },
    { to: '/my', label: '마이' },
]

export default function BottomNav() {
    return (
        <nav className="fixed bottom-0 left-1/2 w-full max-w-md -translate-x-1/2 border-t border-gray-200 bg-white">
            <ul className="grid grid-cols-4">
                {tabs.map((tab) => (
                    <li key={tab.to}>
                        <NavLink
                            to={tab.to}
                            end={tab.end}
                            className={({ isActive }) =>
                                `flex h-14 items-center justify-center text-sm ${
                                    isActive ? 'font-bold text-gray-900' : 'text-gray-400'
                                }`
                            }
                        >
                            {tab.label}
                        </NavLink>
                    </li>
                ))}
            </ul>
        </nav>
    )
}