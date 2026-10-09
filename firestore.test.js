const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read user document", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.doc(`users/${ALICE_UID}`).get());
});

test("Authenticated user: cannot read another user's document", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().doc(`users/${ALICE_UID}`).set({
      userId: ALICE_UID,
      fullName: "Alice Wonderland",
      balanceGds: 1500,
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.doc(`users/${ALICE_UID}`).get());
});

test("Authenticated user: can read and create their own document", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.doc(`users/${ALICE_UID}`).set({
      userId: ALICE_UID,
      fullName: "Alice Wonderland",
      balanceGds: 500,
    })
  );

  await assertSucceeds(aliceDb.doc(`users/${ALICE_UID}`).get());
});

test("Authenticated user: cannot create user with negative balance", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(
    aliceDb.doc(`users/${ALICE_UID}`).set({
      userId: ALICE_UID,
      fullName: "Alice Wonderland",
      balanceGds: -100,
    })
  );
});

test("Authenticated user: can create and view their own transactions", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.doc(`users/${ALICE_UID}/transactions/tx_101`).set({
      id: "tx_101",
      userId: ALICE_UID,
      type: "DEPOSIT",
      amountGds: 500,
      status: "SUCCESS",
    })
  );

  await assertSucceeds(aliceDb.collection(`users/${ALICE_UID}/transactions`).get());
});

test("Authenticated user: cannot read another user's transactions", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().doc(`users/${ALICE_UID}/transactions/tx_101`).set({
      id: "tx_101",
      userId: ALICE_UID,
      type: "DEPOSIT",
      amountGds: 500,
      status: "SUCCESS",
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.doc(`users/${ALICE_UID}/transactions/tx_101`).get());
});
