import puppeteer from "puppeteer";
import { setDefaultOptions } from "expect-puppeteer";

setDefaultOptions({ timeout: 5000 });

describe("Smoke test", () => {
    let browser;
    let page;

    beforeAll(async () => {
        browser = await puppeteer.launch({ headless: true });
        page = await browser.newPage();
    });

    afterAll(async () => {
        await browser.close();
    });

    test("integrates with the backend", async () => {
        await page.goto(process.env.APP_URL || "http://localhost:8080");
        await expect(page).toMatchTextContent(/Backend server is UP/);
    }, 10_000);

});
