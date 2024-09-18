import Main from "@/pages/Main";
import InvestTransactions from "@/pages/InvestTransactions";
import Replenishments from "@/pages/Replenishments";
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
];

const router = createRouter({
  routes,
  history: createWebHistory(process.env.BASE_URL),
});

export default router;
