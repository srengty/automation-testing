describe('Login Feature', () => {
  beforeEach(() => {
    cy.visit('http://localhost:3000/');
  });

  it('should successfully login with correct credentials', () => {
    cy.intercept('POST', '**/api/login', {
      statusCode: 200,
      body: { status: 'success' }
    }).as('loginRequest');
    
    cy.get('input[name="username"]').type('admin');
    cy.get('input[name="password"]').type('admin123');
    
    cy.get('button[type="submit"]').click();
    cy.wait('@loginRequest').its('response.body').should('have.property', 'status', 'success');
    cy.url().should('include', '/dashboard');
  });

  it('should fail login with incorrect password', () => {
    cy.intercept('POST', '**/api/login', {
      statusCode: 401,
      body: { status: 'fail' }
    }).as('loginRequest');
    
    cy.get('input[name="username"]').type('admin');
    cy.get('input[name="password"]').type('admin');
    cy.get('button[type="submit"]').click();
    cy.wait('@loginRequest').its('response.body').should('have.property', 'status', 'fail');
    cy.get('#errorMessage').should('be.visible');
  });
});
