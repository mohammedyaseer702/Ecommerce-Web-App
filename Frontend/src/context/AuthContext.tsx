import { createContext, useContext, useEffect, useState } from "react";
import type { AuthContextType, AuthUser } from "../types/auth";
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface JwtPayload {
  sub: string;
  role: string;
  exp: number;
}

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<AuthUser | null>(null);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      const decoded = jwtDecode<JwtPayload>(token);
      setUser({
        email: decoded.sub,
        role: decoded.role,
      });
    }
  }, []);

  const login = (token: string) => {
    localStorage.setItem("accessToken", token);
    const decoded = jwtDecode<JwtPayload>(token);
    setUser({
      email: decoded.sub,
      role: decoded.role,
    });
  };

  const logout = () => {
    localStorage.removeItem("accessToken");
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        isAuthenticated: !!user,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within AuthProvider");
  return context;
};
