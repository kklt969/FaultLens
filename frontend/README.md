# FaultLens frontend
    cd frontend
    npm install
    npm run dev      # http://localhost:5173  (backend expected at http://localhost:8081)
If the browser reports a CORS error, either add `@Configuration` to `CorsConfig.java`, or set `VITE_API_BASE_URL=` (empty) in `.env` to route requests through the Vite proxy.
