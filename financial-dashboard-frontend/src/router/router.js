import Main from "@/pages/Main";
import InvestTransactions from "@/pages/InvestTransactions";
import Replenishments from "@/pages/Replenishments";
import InvestmentsPortfolio from "@/pages/InvestmentsPortfolio";
import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    component: Main,
  },
  {
    path: "/invest-transactions",
    component: InvestTransactions,
  },
  {
    path: "/replenishments",
    component: Replenishments,
  },
  {
    path: "/investments-portfolio",
    component: InvestmentsPortfolio,
  },
];

const router = createRouter({
  routes,
  history: createWebHistory(process.env.BASE_URL),
});

export default router;