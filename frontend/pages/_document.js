import { Html, Head, Main, NextScript } from 'next/document'
import { ThemeProvider } from '@/components/theme-provider'
import axios from 'axios'

export default function Document() {
  axios.defaults.baseURL = "http://localhost:8080"
  return (
    <Html lang="en">
      <Head />
      <body>
        <ThemeProvider
          attribute="class"
          defaultTheme="system"
          enableSystem
          disableTransitionOnChange
        >
          <Main />
          <NextScript />
        </ThemeProvider>
      </body>
    </Html>
  )
}
