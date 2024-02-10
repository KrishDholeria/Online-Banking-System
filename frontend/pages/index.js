import Image from 'next/image'
import { Inter } from 'next/font/google'

const inter = Inter({ subsets: ['latin'] })

export default function Home() {
  return (
    <div>
      <h1>Next.js + Inter</h1>
      <Image
        src="/inter.png"
        alt="Inter"
        width={400}
        height={400}
      />
    </div>
  )
}
