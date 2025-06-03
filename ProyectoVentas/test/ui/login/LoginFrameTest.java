package ui.login;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

// It's often good practice to avoid star imports for Swing in test files if possible,
// but for brevity in this context, it's acceptable.
// For a real project, consider specific imports.
import javax.swing.*;
import java.awt.HeadlessException;


// Project specific classes
import entidades.Administrador;
import seguridad.Session;
import datos.AdministradorDatos;
import servicios.ServicioLogin;
// ui.menu.Menu_Principal and ui.admin.CrearAdminFrame are not directly instantiated
// in a way that's easily verifiable in unit tests without more complex setups.
// We will verify by checking if LoginFrame is disposed.

// Material UI components
import material.components.MaterialButton;
import material.components.MaterialTextField;
import material.components.MaterialPasswordField;

// Simple stub classes for mocking dependencies
class MockAdministradorDatos extends AdministradorDatos {
    boolean mockExisteAdminMaestro = false;
    int existeAdminMaestroCallCount = 0;

    public MockAdministradorDatos() { }

    @Override
    public boolean existeAdminMaestro() {
        existeAdminMaestroCallCount++;
        return mockExisteAdminMaestro;
    }

    public void setExisteAdminMaestro(boolean value) {
        this.mockExisteAdminMaestro = value;
    }
}

class MockServicioLogin extends ServicioLogin {
    Administrador mockAdminToReturn = null;
    RuntimeException exceptionToThrow = null;
    int autenticarCallCount = 0;
    String lastUserAttempted = null;
    String lastPasswordAttempted = null;


    public MockServicioLogin() { }

    @Override
    public Administrador autenticar(String usuario, String contraseña) throws RuntimeException {
        autenticarCallCount++;
        lastUserAttempted = usuario;
        lastPasswordAttempted = contraseña;
        if (exceptionToThrow != null) {
            throw exceptionToThrow;
        }
        if (mockAdminToReturn != null) {
            return mockAdminToReturn;
        }
        // Default behavior: throw if not configured, to catch unexpected calls
        throw new IllegalStateException("MockServicioLogin.autenticar was called without being configured.");
    }

    public void setAdminToReturn(Administrador admin) {
        this.mockAdminToReturn = admin;
        this.exceptionToThrow = null;
    }

    public void setExceptionToThrow(RuntimeException ex) {
        this.exceptionToThrow = ex;
        this.mockAdminToReturn = null;
    }
}


@Ignore("GUI tests are skipped in headless environment")
public class LoginFrameTest {

    private MockAdministradorDatos mockAdminDatos;
    private MockServicioLogin mockServicioLogin;
    private LoginFrame loginFrame;

    // Static block to prevent HeadlessException during tests that might involve Swing components
    static {
        System.setProperty("java.awt.headless", "true");
    }

    @Before
    public void setUp() {
        // Reset session before each test
        Session.setIdAdmin(-1);

        mockAdminDatos = new MockAdministradorDatos();
        mockServicioLogin = new MockServicioLogin();
        // Note: LoginFrame constructor calls initComponents and other logic
        // based on these mocks immediately.
    }

    private void createLoginFrame() {
        // Create frame using mocks. This must be done AFTER mocks are configured for constructor logic.
        try {
            loginFrame = new LoginFrame(mockAdminDatos, mockServicioLogin);
        } catch (HeadlessException e) {
            System.err.println("HeadlessException during LoginFrame creation in test. " +
                               "Ensure 'java.awt.headless' is true. Current: " + System.getProperty("java.awt.headless"));
            throw e;
        }
    }

    @Test
    public void testComponentInitialization_NoMasterAdmin() {
        mockAdminDatos.setExisteAdminMaestro(false);
        createLoginFrame();

        assertNotNull("UsuarioTextField should be initialized", loginFrame.UsuarioTextField);
        assertTrue("UsuarioTextField should be a MaterialTextField", loginFrame.UsuarioTextField instanceof MaterialTextField);

        assertNotNull("jPasswordField1 should be initialized", loginFrame.jPasswordField1);
        assertTrue("jPasswordField1 should be a MaterialPasswordField", loginFrame.jPasswordField1 instanceof MaterialPasswordField);

        assertNotNull("btnIniciarSesion should be initialized", loginFrame.btnIniciarSesion);
        assertTrue("btnIniciarSesion should be a MaterialButton", loginFrame.btnIniciarSesion instanceof MaterialButton);

        assertNotNull("BtnCrearAdmin should be initialized", loginFrame.BtnCrearAdmin);
        assertTrue("BtnCrearAdmin should be a MaterialButton", loginFrame.BtnCrearAdmin instanceof MaterialButton);
    }

    @Test
    public void testInitialButtonVisibility_NoMasterAdmin() {
        mockAdminDatos.setExisteAdminMaestro(false);
        createLoginFrame(); // Constructor uses adminDatos

        assertTrue("BtnCrearAdmin should be visible if no master admin exists", loginFrame.BtnCrearAdmin.isVisible());
        assertFalse("btnIniciarSesion should be hidden if no master admin exists", loginFrame.btnIniciarSesion.isVisible());
        assertEquals(1, mockAdminDatos.existeAdminMaestroCallCount);
    }

    @Test
    public void testInitialButtonVisibility_MasterAdminExists() {
        mockAdminDatos.setExisteAdminMaestro(true);
        createLoginFrame(); // Constructor uses adminDatos

        assertFalse("BtnCrearAdmin should be hidden if master admin exists", loginFrame.BtnCrearAdmin.isVisible());
        assertTrue("btnIniciarSesion should be visible if master admin exists", loginFrame.btnIniciarSesion.isVisible());
        assertEquals(1, mockAdminDatos.existeAdminMaestroCallCount);
    }

    @Test
    public void testLoginSuccess() {
        mockAdminDatos.setExisteAdminMaestro(true); // So IniciarSesion button is visible
        Administrador dummyAdmin = new Administrador(1, "test_admin", "password_hash", "Test Admin", "", true, true);
        mockServicioLogin.setAdminToReturn(dummyAdmin);
        createLoginFrame();

        loginFrame.UsuarioTextField.setText("test_admin");
        loginFrame.jPasswordField1.setText("password");

        // Simulate button click
        // Directly calling actionPerformed is cleaner than doClick() for unit tests if possible
        loginFrame.btnIniciarSesion.getActionListeners()[0].actionPerformed(null);
        // loginFrame.btnIniciarSesionActionPerformed(null); // Alternative if method is public/package-private

        assertEquals(1, mockServicioLogin.autenticarCallCount);
        assertEquals("test_admin", mockServicioLogin.lastUserAttempted);
        assertEquals("password", mockServicioLogin.lastPasswordAttempted);
        assertEquals(dummyAdmin.id(), Session.getIdAdmin());
        assertTrue("LoginFrame should be disposed on successful login", loginFrame.isDisposedForTest);
        // Verifying JOptionPane is hard. We assume success if disposed and session is set.
    }

    @Test
    public void testLoginFailure_InvalidCredentials() {
        mockAdminDatos.setExisteAdminMaestro(true); // So IniciarSesion button is visible
        mockServicioLogin.setExceptionToThrow(new RuntimeException("Credenciales inválidas"));
        createLoginFrame();

        loginFrame.UsuarioTextField.setText("wrong_user");
        loginFrame.jPasswordField1.setText("wrong_pass");

        loginFrame.btnIniciarSesion.getActionListeners()[0].actionPerformed(null);

        assertEquals(1, mockServicioLogin.autenticarCallCount);
        assertEquals("wrong_user", mockServicioLogin.lastUserAttempted);
        assertEquals("wrong_pass", mockServicioLogin.lastPasswordAttempted);
        assertEquals(-1, Session.getIdAdmin()); // Session ID should not be set
        assertFalse("LoginFrame should NOT be disposed on failed login", loginFrame.isDisposedForTest);
        // Verifying JOptionPane is hard. We assume failure if not disposed and session not set.
    }

    @Test
    public void testNavigateToCrearAdminFrame() {
        mockAdminDatos.setExisteAdminMaestro(false); // So BtnCrearAdmin is visible
        createLoginFrame();

        loginFrame.BtnCrearAdmin.getActionListeners()[0].actionPerformed(null);
        // loginFrame.BtnCrearAdminActionPerformed(null); // Alternative

        assertTrue("LoginFrame should be disposed when navigating to CrearAdminFrame", loginFrame.isDisposedForTest);
        // Verifying CrearAdminFrame visibility is hard. Dispose implies navigation.
    }

    @Test
    public void testUsuarioTextFieldEnterFocusesPasswordField() {
        mockAdminDatos.setExisteAdminMaestro(true); // Arbitrary, just to init components
        createLoginFrame();

        // To test focus, we'd ideally check `jPasswordField1.hasFocus()`.
        // However, `hasFocus()` in a headless environment without a visible, focused window might be unreliable.
        // For now, we'll trust the `requestFocus()` call is made.
        // A more robust test would require a UI testing framework or Robot class.
        // This test is more of a placeholder for that concept.

        // loginFrame.UsuarioTextFieldActionPerformed(null); // Call the action directly
        // assertTrue(loginFrame.jPasswordField1.hasFocus()); // This might not work reliably.
        // As a simplification, we'll just ensure the action doesn't throw an error.
        try {
            loginFrame.UsuarioTextField.getActionListeners()[0].actionPerformed(null);
        } catch (Exception e) {
            fail("UsuarioTextFieldActionPerformed should not throw an exception: " + e.getMessage());
        }
        // A truly reliable test of focus change is beyond simple JUnit here.
    }

    @Test
    public void testPasswordFieldEnterTriggersLogin() {
        mockAdminDatos.setExisteAdminMaestro(true); // So IniciarSesion button is visible
        Administrador dummyAdmin = new Administrador(1, "test_admin_enter", "password_enter_hash", "Test Admin Enter", "", true, true);
        mockServicioLogin.setAdminToReturn(dummyAdmin); // Login should succeed
        createLoginFrame();

        loginFrame.UsuarioTextField.setText("test_admin_enter");
        loginFrame.jPasswordField1.setText("password_enter");

        // loginFrame.jPasswordField1ActionPerformed(null); // Call the action directly
        loginFrame.jPasswordField1.getActionListeners()[0].actionPerformed(null);


        assertEquals("Login action should be triggered", 1, mockServicioLogin.autenticarCallCount);
        assertEquals("test_admin_enter", mockServicioLogin.lastUserAttempted);
        assertEquals("password_enter", mockServicioLogin.lastPasswordAttempted);
        assertEquals(dummyAdmin.id(), Session.getIdAdmin());
        assertTrue("LoginFrame should be disposed on successful login via Enter key", loginFrame.isDisposedForTest);
    }
}
