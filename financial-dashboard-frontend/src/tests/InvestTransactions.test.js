import { render } from "@testing-library/vue";
import InvestTransactions from "@/pages/InvestTransactions.vue";

test('render Invest Transactions component', () => {
    const { debug } = render(InvestTransactions);

    debug()
})