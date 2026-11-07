import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { HashRouter, Route, Routes } from "react-router-dom";
import "./index.css";
import Layout from "./components/Layout";
import Dashboard from "./pages/Dashboard";
import Experiments from "./pages/Experiments";
import Create from "./pages/Create";
import Details from "./pages/Details";
import Compare from "./pages/Compare";
import Reports from "./pages/Reports";
import Settings from "./pages/Settings";
createRoot(document.getElementById("root")!).render(
  <StrictMode><HashRouter><Routes><Route element={<Layout />}>
    <Route index element={<Dashboard />} /><Route path="experiments" element={<Experiments />} /><Route path="experiments/:id" element={<Details />} />
    <Route path="create" element={<Create />} /><Route path="comparisons" element={<Compare />} /><Route path="reports" element={<Reports />} /><Route path="settings" element={<Settings />} />
  </Route></Routes></HashRouter></StrictMode>);
