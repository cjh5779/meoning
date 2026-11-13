import { Outlet } from 'react-router'
import BottomNav from './BottomNav'

export default function AppLayout() {
    return (
        <div className="mx-auto min-h-screen max-w-md bg-white pb-16">
            <Outlet />
            <BottomNav />
        </div>
    )
}