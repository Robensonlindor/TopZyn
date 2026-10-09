package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.TopZynSubPageHeader
import com.example.ui.theme.TopZynBackground
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynDarkCard
import com.example.ui.theme.TopZynGold
import com.example.ui.theme.TopZynGreen
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynOrangeContainer
import com.example.ui.theme.TopZynTextMuted
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite
import kotlinx.coroutines.delay

// 1. Écran de démarrage (Splash Screen)
@Composable
fun SplashScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(1800)
        onContinue()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        TopZynDarkBlack,
                        Color(0xFF1E1E24),
                        TopZynDarkBlack
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF18181B))
                    .border(2.dp, TopZynOrange, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_topzyn_logo),
                    contentDescription = "Logo TopZyn",
                    modifier = Modifier.size(86.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Top",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    color = TopZynWhite
                )
                Text(
                    text = "Zyn",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    color = TopZynOrange
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "RECHARGE JEUX & PAIEMENTS MOBILES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynGold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = onContinue,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp)
            ) {
                Text("Entrer sur TopZyn", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

// 2. Écran de bienvenue et de connexion
@Composable
fun WelcomeScreen(
    onNavigateLogin: () -> Unit,
    onNavigateRegister: () -> Unit,
    onContinueGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynWhite)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(TopZynDarkBlack)
                    .border(2.dp, TopZynOrange, RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_topzyn_logo),
                    contentDescription = "Logo TopZyn",
                    modifier = Modifier.size(72.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text("Bienvenue sur ", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)
                Text("TopZyn", fontSize = 26.sp, fontWeight = FontWeight.Black, color = TopZynOrange)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "La première plateforme officielle en Haïti pour recharger vos jeux favoris en Gourdes (GDS) via MonCash & Natcash.",
                fontSize = 13.5.sp,
                color = TopZynTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp
            )
        }

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onNavigateLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("welcome_login_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Text("Se connecter", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            com.example.auth.GoogleSignInButton(
                buttonText = "Connexion rapide avec Google",
                onAuthSuccess = { onNavigateLogin() },
                onAuthError = {}
            )

            OutlinedButton(
                onClick = onNavigateRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("welcome_register_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TopZynDarkBlack
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, TopZynDarkBlack)
            ) {
                Text("Créer un compte TopZyn", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            TextButton(
                onClick = onContinueGuest,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Explorer sans compte", color = TopZynTextSecondary, fontSize = 14.sp)
            }
        }
    }
}

// 3. Écran de connexion
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateForgotPassword: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var emailOrPhone by remember { mutableStateOf("jmbaptiste@topzyn.com") }
    var password by remember { mutableStateOf("123456") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynWhite)
    ) {
        TopZynSubPageHeader(
            title = "Connexion",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Heureux de vous revoir !",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )

            Text(
                text = "Connectez-vous pour accéder à votre solde et vos commandes",
                fontSize = 13.sp,
                color = TopZynTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Email or Phone
            OutlinedTextField(
                value = emailOrPhone,
                onValueChange = {
                    emailOrPhone = it
                    errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_identity_input"),
                label = { Text("E-mail ou Téléphone (+509)") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TopZynOrange) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_password_input"),
                label = { Text("Mot de passe") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TopZynOrange) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Remember me & Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = TopZynOrange)
                    )
                    Text("Se souvenir de moi", fontSize = 12.5.sp, color = TopZynTextSecondary)
                }

                TextButton(onClick = onNavigateForgotPassword) {
                    Text("Mot de passe oublié ?", fontSize = 12.5.sp, color = TopZynOrange, fontWeight = FontWeight.Bold)
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.5.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    if (emailOrPhone.isBlank() || password.isBlank()) {
                        errorMessage = "Veuillez remplir tous les champs"
                        return@Button
                    }
                    onLoginSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("login_submit_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Text("Connexion", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = TopZynCardBorder)
                Text("  OU  ", fontSize = 12.sp, color = TopZynTextSecondary, fontWeight = FontWeight.Bold)
                HorizontalDivider(modifier = Modifier.weight(1f), color = TopZynCardBorder)
            }

            Spacer(modifier = Modifier.height(14.dp))

            com.example.auth.GoogleSignInButton(
                buttonText = "Continuer avec Google",
                onAuthSuccess = {
                    onLoginSuccess()
                },
                onAuthError = { err ->
                    errorMessage = err
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register redirect
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Pas encore de compte ?", fontSize = 13.5.sp, color = TopZynTextSecondary)
                TextButton(onClick = onNavigateRegister) {
                    Text("Créer un compte", fontSize = 13.5.sp, color = TopZynOrange, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 4. Écran de création de compte
@Composable
fun RegisterScreen(
    onRegisterSuccess: (phoneOrEmail: String) -> Unit,
    onNavigateLogin: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+509 ") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynWhite)
    ) {
        TopZynSubPageHeader(
            title = "Création de compte",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rejoignez TopZyn",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )
            Text(
                text = "Créez votre portefeuille gratuit pour recharger instantanément",
                fontSize = 13.sp,
                color = TopZynTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Full Name
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nom complet") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = TopZynOrange) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Phone Number
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Numéro de téléphone (MonCash/Natcash)") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = TopZynOrange) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Adresse email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TopZynOrange) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Mot de passe sécurisé") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TopZynOrange) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Terms checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = acceptTerms,
                    onCheckedChange = { acceptTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = TopZynOrange)
                )
                Text(
                    text = "J'accepte les conditions d'utilisation et la politique de confidentialité TopZyn",
                    fontSize = 12.sp,
                    color = TopZynTextSecondary
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.5.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (fullName.isBlank() || phone.isBlank() || password.isBlank()) {
                        errorMessage = "Veuillez remplir les informations obligatoires"
                        return@Button
                    }
                    if (!acceptTerms) {
                        errorMessage = "Veuillez accepter les conditions d'utilisation"
                        return@Button
                    }
                    onRegisterSuccess(phone)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Text("Créer mon compte", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Vous avez déjà un compte ?", fontSize = 13.5.sp, color = TopZynTextSecondary)
                TextButton(onClick = onNavigateLogin) {
                    Text("Se connecter", fontSize = 13.5.sp, color = TopZynOrange, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 5. Écran de vérification du compte (OTP)
@Composable
fun VerifyOtpScreen(
    contact: String,
    onVerifySuccess: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var otpCode by remember { mutableStateOf("") }
    var secondsLeft by remember { mutableIntStateOf(59) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynWhite)
    ) {
        TopZynSubPageHeader(
            title = "Vérification OTP",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(TopZynOrange.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TopZynOrange, modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Entrez le code de vérification",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )

            Text(
                text = "Un code à 6 chiffres a été envoyé par SMS à $contact",
                fontSize = 13.sp,
                color = TopZynTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = otpCode,
                onValueChange = {
                    if (it.length <= 6) {
                        otpCode = it
                        errorMessage = null
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .testTag("otp_code_input"),
                placeholder = { Text("654321", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 8.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (secondsLeft > 0) {
                Text("Renvoyer le code dans 00:${if (secondsLeft < 10) "0$secondsLeft" else secondsLeft}", fontSize = 12.5.sp, color = TopZynTextMuted)
            } else {
                TextButton(onClick = { secondsLeft = 59 }) {
                    Text("Renvoyer un nouveau code", color = TopZynOrange, fontWeight = FontWeight.Bold)
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (otpCode.length < 4) {
                        errorMessage = "Veuillez entrer le code reçu"
                        return@Button
                    }
                    onVerifySuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Text("Valider mon compte", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 6. Écran de récupération du mot de passe
@Composable
fun ForgotPasswordScreen(
    onSendResetLink: (email: String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var emailOrPhone by remember { mutableStateOf("") }
    var messageSent by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynWhite)
    ) {
        TopZynSubPageHeader(
            title = "Récupération de mot de passe",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mot de passe oublié ?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )

            Text(
                text = "Saisissez votre e-mail ou numéro de téléphone pour recevoir les instructions de réinitialisation.",
                fontSize = 13.sp,
                color = TopZynTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = emailOrPhone,
                onValueChange = { emailOrPhone = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("E-mail ou Téléphone") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (emailOrPhone.isNotBlank()) {
                        messageSent = true
                        onSendResetLink(emailOrPhone)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Text("Envoyer les instructions", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            if (messageSent) {
                Spacer(modifier = Modifier.height(18.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TopZynGreen)
                ) {
                    Text(
                        text = "✓ Instructions envoyées avec succès à $emailOrPhone !",
                        color = TopZynGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }
    }
}

// 7. Écran de réinitialisation du mot de passe
@Composable
fun ResetPasswordScreen(
    onResetSuccess: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynWhite)
    ) {
        TopZynSubPageHeader(
            title = "Nouveau mot de passe",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Choisissez un nouveau mot de passe",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nouveau mot de passe") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirmer le mot de passe") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.5.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (newPassword.length < 6) {
                        errorMessage = "Le mot de passe doit contenir au moins 6 caractères"
                        return@Button
                    }
                    if (newPassword != confirmPassword) {
                        errorMessage = "Les deux mots de passe ne correspondent pas"
                        return@Button
                    }
                    onResetSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Text("Enregistrer et se connecter", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 8. Écran de déconnexion avec confirmation
@Composable
fun LogoutConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Déconnexion TopZyn", fontWeight = FontWeight.Bold)
        },
        text = {
            Text("Êtes-vous sûr de vouloir vous déconnecter de votre compte TopZyn ?")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
            ) {
                Text("Oui, me déconnecter")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler", color = TopZynTextSecondary)
            }
        }
    )
}
