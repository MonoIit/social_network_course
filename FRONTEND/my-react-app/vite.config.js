import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  define: {
    global: {}
  },
  server: {
    port: 5173,
    proxy: {
      // Проксируем все запросы к /ws и /app на backend
      '/ws': {
        target: 'http://localhost:8084',
        ws: true,          // важно для WebSocket
        changeOrigin: true
      },
      '/app': {
        target: 'http://localhost:8084',
        changeOrigin: true
      }
    }
  }
})
