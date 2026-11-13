import { createBrowserRouter } from 'react-router'
import AppLayout from './components/layout/AppLayout'
import HomePage from './pages/HomePage'
import BenefitDetailPage from './pages/BenefitDetailPage'
import ComingSoonPage from './pages/ComingSoonPage'

export const router = createBrowserRouter([
    {
        path: '/',
        element: <AppLayout />,
        children: [
            { index: true, element: <HomePage /> },
            { path: 'calendar', element: <ComingSoonPage title="혜택 캘린더" /> },
            { path: 'bookmarks', element: <ComingSoonPage title="내 찜" /> },
            { path: 'my', element: <ComingSoonPage title="마이" /> },
        ],
    },
    { path: '/benefits/:benefitId', element: <BenefitDetailPage /> },
])