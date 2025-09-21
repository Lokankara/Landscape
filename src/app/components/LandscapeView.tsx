import React, { useEffect, useRef, useState } from 'react'
import { fetchLandscape, Point3D } from '../api'


function project(p: Point3D, w: number, h: number) {
    const scale = 40
    return { x: w / 2 + p.x * scale, y: h / 2 - p.y * scale, z: p.z }
}


export default function LandscapeView() {
    const [points, setPoints] = useState<Point3D[]>([])
    const svgRef = useRef<SVGSVGElement | null>(null)
    const [loading, setLoading] = useState(false)


    useEffect(() => { load() }, [])


    async function load() {
        setLoading(true)
        try {
            const pts = await fetchLandscape()
            setPoints(pts)
        } catch (err) {
            console.error(err)
        } finally { setLoading(false) }
    }


    return (
        <div className="bg-white p-4 rounded shadow">
            <div className="flex items-center justify-between mb-3">
                <h2 className="font-semibold">Potential Landscape</h2>
                <div className="text-sm text-slate-500">{loading ? 'Loading...' : `${points.length} pts`}</div>
            </div>
            <div className="w-full h-[600px]">
                <svg ref={svgRef} width="100%" height="100%" viewBox={`0 0 900 600`}>
                    <rect width="100%" height="100%" fill="#f8fafc" />
                    {points.map((p, i) => {
                        const pt = project(p, 900, 600)
                        const color = p.z > 0 ? 'red' : 'blue'
                        const r = Math.max(2, Math.min(6, 6 - Math.abs(p.z)))
                        return <circle key={i} cx={pt.x} cy={pt.y} r={r} fill={color} opacity={0.8} />
                    })}
                </svg>
            </div>
        </div>
    )
}